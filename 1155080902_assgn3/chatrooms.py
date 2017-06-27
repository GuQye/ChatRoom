from flask import Flask,jsonify,request
import MySQLdb.cursors
import MySQLdb
import time
app = Flask(__name__)

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
				db = "iems5722",
				use_unicode = True,
				charset = "utf8",
				cursorclass = MySQLdb.cursors.DictCursor
				)
		return

#mydb = MySQLdb.connect("localhost", "root", "guquanye123", "iems5722")
@app.route('/api/asgn3/get_chatrooms')
def get_chatrooms():
	mydb = MyDatabase()
	
	query = "select * from chatrooms"
	cursor = mydb.db.cursor()
	cursor.execute(query)
	chatrooms = cursor.fetchall()
	return jsonify(data=chatrooms,status="OK")

@app.route('/api/asgn3/get_messages')
def get_messages():
	mydb = MyDatabase()
	chatroom_id = request.args.get("chatroom_id",0,type = int)
	page = request.args.get("page","0",type = int)
	query = "select chatroom_id,user_id,name,message,timestamp from messages where chatroom_id = %s order by id desc" %(chatroom_id)
	cursor = mydb.db.cursor()
	cursor.execute(query)
	messages = cursor.fetchall()
	cunt = "select count(id) as count from messages where chatroom_id = %s" %chatroom_id
	cursor.execute(cunt)
	count = cursor.fetchone().get('count')
	if count%20==0:
		total_pages = count/20
	else:
		total_pages = count/20+1
	if page>total_pages:
		return jsonify(data = [],total_pages=total_pages,status="no more data")
	lenth = len(messages)
	if lenth < page*20:
		return jsonify(data = messages[(page-1)*20:],page=page,total_pages = total_pages,status="OK")
	else:
		return jsonify(data = messages[(page-1)*20:page*20],page = page,total_pages = total_pages,status="OK")



@app.route('/api/asgn3/send_message',methods=['POST'])
def send_message():
	mydb = MyDatabase()
	chatroom_id = request.form.get("chatroom_id") 
	user_id = request.form.get("user_id")
	name = request.form.get("name")
	message =  request.form.get("message")
	print chatroom_id
	


	if message == None or chatroom_id == None or chatroom_id == '' or not chatroom_id.isdigit() or name == None or user_id == None :
		return	jsonify(status="error",data = "the paras can not be empty")
	query = "insert into messages(chatroom_id,user_id,name,message,timestamp) values (%s,%s,%s,%s,%s)" 
	ttTuple = time.localtime()
	ttList = list(ttTuple)
	ttList[3] = ttList[3] + 8
	ttTuple = tuple(ttList)
	params = (chatroom_id,user_id,name,message,time.strftime("%Y-%m-%d %H:%M:%S",ttTuple))
	cursor = mydb.db.cursor()
	cursor.execute(query,params)
	mydb.db.commit()
	return jsonify(status="OK")
if __name__=='__main__':
	app.debug=True
	app.run(host='0.0.0.0')




