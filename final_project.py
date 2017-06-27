import os
from celery import Celery
#from task import send_to_fcm
from flask import Flask,jsonify,request,redirect, url_for,send_from_directory
from werkzeug import secure_filename
import MySQLdb.cursors
import MySQLdb
import time
import random

UPLOAD_FOLDER = './record'
ALLOWED_EXTENSIONS = set(['rar','pptx','JPG','txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif','amr','mp4'])
app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

class MyDatabase:
	db = None
	def __init__(self):
		self.connect()
		return
	def connect(self):
		self.db = MySQLdb.connect(
				host = "localhost",
				port = 3306,
				user = "root",
				passwd = "guquanye123",
				db = "project",
				use_unicode = True,
				charset = "utf8",
				cursorclass = MySQLdb.cursors.DictCursor
				)
		return

@app.route('/x',methods=['POST'])
def x():
	mydb = MyDatabase()
	account = request.form.get("account")
	password = request.form.get("password")
	query = "select * from chatrooms"
	cursor = mydb.db.cursor()
	cursor.execute(query)
	chatrooms = cursor.fetchall()
	return jsonify(data=chatrooms,status="OK")


@app.route('/message',methods=['POST'])
def message():
    mydb = MyDatabase()
    room_id = request.form.get("room_id")
    user_id = request.form.get("user_id")
    print user_id
    m_type = request.form.get("type")
    message = request.form.get("message")
    query = "insert into message (room_id,user_id,type,message) values ('%s','%s','%s','%s')" %(room_id,user_id,m_type,message)
    cursor = mydb.db.cursor()
    cursor.execute(query)
    mydb.db.commit()
    return jsonify(status="OK")


@app.route('/show_message',methods=['POST'])
def show_message():
    mydb = MyDatabase()
    room_id = request.form.get("room_id")
    message_id = request.form.get("message_id")
    query = "select id,user_id,message,type from message where room_id='%s' and id>'%s'" %(room_id,message_id)
    cursor = mydb.db.cursor()
    cursor.execute(query)
    message = cursor.fetchall()
    return jsonify(status="OK",messages=message)


@app.route('/word',methods=['POST'])
def word():
    mydb = MyDatabase()
    word = request.form.get("word")
    query = "insert into questions (words) values ('%s')" %word
    cursor = mydb.db.cursor()
    cursor.execute(query)
    mydb.db.commit()
    return jsonify(status="OK") 


@app.route('/init_room',methods=['POST'])
def init_room():
    mydb = MyDatabase()
    word_id = random.randint(1,6)
    m_id = [0,2,2,2,1,1]
    room_id = request.form.get("room_id")
    rnd = request.form.get("round")
    rnd = int(rnd)
    print rnd
    query = "select room_detail.user_name,users.id from users,room_detail where users.user_name = room_detail.user_name and room_detail.room_id='%s'" %room_id
    cursor = mydb.db.cursor()
    cursor.execute(query)
    users = cursor.fetchall()
    query = "select words from questions where id = '%s'" %word_id
    cursor = mydb.db.cursor()
    cursor.execute(query)
    words = cursor.fetchone().get('words')
    print room_id + "!!!"
    query = "select user_id from room_detail where room_id='%s'" %room_id
    cursor = mydb.db.cursor()
    cursor.execute(query)
    user_list = cursor.fetchall()
    m = m_id[rnd]
    print user_list[m]
    master_id = user_list[m].get('user_id')
     
    query="delete from message where room_id='%s'" %room_id
    cursor = mydb.db.cursor()
    cursor.execute(query)
    mydb.db.commit()
    return jsonify(users=users,word=words,master_id=3,status="OK")




@app.route('/signin',methods=['POST'])
def sign_in():
    mydb = MyDatabase()
    account = request.form.get("account")
    password = request.form.get("password")
    query = "select id,user_name,password from users where account = '%s'" %account
    cursor = mydb.db.cursor()
    cursor.execute(query)
    user = cursor.fetchone()
    pwd = user.get('password')
    user_name = user.get('user_name')
    user_id = user.get('id')
    print pwd
    if(pwd == password):
		return jsonify(data=200,user_id=user_id,user_name = user_name,status="OK")
    else:
		return jsonify(data=404,ststus="ERROR")

@app.route('/signup',methods=['POST'])
def sign_up():
    mydb = MyDatabase()
    user_name = request.form.get("user_name")
    account = request.form.get("account")
    password = request.form.get("password")
    query = "insert into users (user_name,account,password) values ('%s','%s','%s')" %(user_name,account,password)
    cursor = mydb.db.cursor()
    cursor.execute(query)
    mydb.db.commit()
    return jsonify(status="OK")



@app.route('/get_rooms')
def get_rooms():
	mydb = MyDatabase()
	query = "select rooms.id,rooms.name,count(room_detail.id) as num from rooms,room_detail where rooms.id = room_detail.room_id group by room_detail.room_id"
	cursor = mydb.db.cursor()
	cursor.execute(query)
	rooms = cursor.fetchall()
	return jsonify(data = rooms,status="OK")


@app.route('/enter_room',methods=['POST'])
def enter_room():
	mydb = MyDatabase()
	room_id = request.form.get("room_id")
	user_name = request.form.get("user_name") 
	user_id = request.form.get("user_id")
	query = "insert ignore into room_detail (room_id,user_name,user_id) value ('%s','%s','%s')" %(room_id,user_name,user_id)
	print user_id
	cursor = mydb.db.cursor()
	cursor.execute(query)
	mydb.db.commit()
	return jsonify(status="OK")











@app.route('/leave_room',methods=['POST'])
def leave_room():
    mydb = MyDatabase()
    room_id = request.form.get("room_id")
    user_name = request.form.get("user_name")
    query = "delete from room_detail where room_id = %s and user_name='%s'" %(room_id,user_name)
    print room_id
    cursor = mydb.db.cursor()
    cursor.execute(query)
    mydb.db.commit()
    return jsonify(status="OK")









@app.route('/get_player',methods=['POST'])
def get_player():
	mydb = MyDatabase()
	room_id = request.form.get("room_id")
	query = "select room_detail.user_name,users.id from users,room_detail where users.user_name = room_detail.user_name and room_detail.room_id='%s'" %room_id
	cursor = mydb.db.cursor()
	cursor.execute(query)
	players = cursor.fetchall()
	query = "select count(id) as nums from room_detail where room_id = '%s'" %room_id
	cursor = mydb.db.cursor()
	cursor.execute(query)
	nums = cursor.fetchone().get('nums')
	return jsonify(data = players,nums=nums,status="OK")






@app.route('/api/asgn3/submit_push_token',methods=['POST'])
def submit_push_token():
    mydb = MyDatabase()
    user_id = request.form.get("user_id")
    token = request.form.get("token")



    if token==None or user_id == None :
        return  jsonify(status="error",data = "Message erroe")
    query = "insert into push_tokens(user_id,token) values (%s,%s)"
    params = (user_id,token)
    cursor = mydb.db.cursor()
    cursor.execute(query,params)
    mydb.db.commit()
    return jsonify(status="OK")








@app.route('/UploadFile', methods=['POST'])
def upload_file():
    mydb = MyDatabase()
    room_id = request.form.get("room_id")
    user_id = request.form.get("user_id")
    file = request.files['file']
    print file.filename
    if file and allowed_file(file.filename):
        filename = secure_filename(file.filename)
        print filename
        file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
        query = "insert into message (room_id,user_id,message,type) values ('%s','%s','%s',0)" %(room_id,user_id,os.path.join(app.config['UPLOAD_FOLDER'])+'/'+filename) 
        cursor = mydb.db.cursor()
        cursor.execute(query)
        mydb.db.commit()
        return jsonify(status="OK")
    else:
    	return jsonify(status="ERROR", message="Wrong Extensions")



@app.route('/downloadFile',methods=['POST'])
def download():
    mydb = MyDatabase()
    filename = request.form.get("filename")
    print filename
    if os.path.isfile(os.path.join(app.config['UPLOAD_FOLDER'], filename)):
        return send_from_directory(app.config['UPLOAD_FOLDER'],filename,as_attachment=True)
    abort(404)




if __name__=='__main__':
	app.debug=True
	app.run(host='0.0.0.0')




