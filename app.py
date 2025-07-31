from flask import Flask, request, jsonify
from flask_cors import CORS
from flask_mail import Mail, Message
from flask_bcrypt import Bcrypt
from itsdangerous import URLSafeTimedSerializer
import os
from dotenv import load_dotenv
from models.user import create_user, get_user_by_email, verify_user_email

load_dotenv()

app = Flask(__name__)
CORS(app)
bcrypt = Bcrypt(app)

# Secret config
app.config['SECRET_KEY'] = os.getenv('FLASK_SECRET_KEY')
app.config['MAIL_SERVER'] = os.getenv('MAIL_SERVER')
app.config['MAIL_PORT'] = int(os.getenv('MAIL_PORT'))
app.config['MAIL_USE_TLS'] = os.getenv('MAIL_USE_TLS') == 'True'
app.config['MAIL_USERNAME'] = os.getenv('MAIL_USERNAME')
app.config['MAIL_PASSWORD'] = os.getenv('MAIL_PASSWORD')

mail = Mail(app)
s = URLSafeTimedSerializer(app.config['SECRET_KEY'])

@app.route('/signup', methods=['POST'])
def signup():
    data = request.json
    email = data['email']
    password = data['password']

    if not (email.endswith('@gmail.com') or email.endswith('@iittnif.com')):
        return jsonify({'error': 'Only @gmail.com and @iittnif.com domains allowed'}), 400

    if get_user_by_email(email):
        return jsonify({'error': 'User already exists'}), 400

    pw_hash = bcrypt.generate_password_hash(password).decode('utf-8')
    create_user(email, pw_hash)

    token = s.dumps(email, salt='email-verify')
    link = f"http://10.41.0.100:5000/verify/{token}"

    msg = Message('Verify your email', sender=app.config['MAIL_USERNAME'], recipients=[email])
    msg.body = f'Click to verify: {link}'
    mail.send(msg)

    return jsonify({'message': 'Verification link sent to email'})

@app.route('/verify/<token>')
def verify_email(token):
    try:
        email = s.loads(token, salt='email-verify', max_age=3600)
        verify_user_email(email)
        return "Email verified successfully!"
    except:
        return "Invalid or expired token", 400

@app.route('/login', methods=['POST'])
def login():
    data = request.json
    email = data['email']
    password = data['password']

    user = get_user_by_email(email)
    if not user:
        return jsonify({'error': 'User not found'}), 401

    user_id, user_email, user_password, is_verified = user
    if not is_verified:
        return jsonify({'error': 'Email not verified'}), 403

    if not bcrypt.check_password_hash(user_password, password):
        return jsonify({'error': 'Invalid password'}), 401

    return jsonify({'message': 'Login successful'})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)
