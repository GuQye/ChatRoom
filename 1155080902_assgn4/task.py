from celery import Celery
from flask import Flask
import requests

def make_celery(app):
    celery = Celery(app.import_name, broker=app.config['CELERY_BROKER_URL'])
    celery.conf.update(app.config)
    TaskBase = celery.Task

    class ContextTask(TaskBase):
        abstract = True

        def __call__(self, *args, **kwargs):
            with app.app_context():
                return TaskBase.__call__(self, *args, **kwargs)
    celery.Task = ContextTask
    return celery

app = Flask(__name__)
app.config.update(
    CELERY_BROKER_URL='amqp://guest@0.0.0.0'
)
celery = make_celery(app)

@celery.task()
def send_to_fcm(message,chatroom_id, token):
	api_key = 'AAAACpBa7lk:APA91bG7IbI-7ofVLStaPCHufC86UTsh8aj28f3zK-abz__i_a853wI45nEffQU6kL0SAutbMDM0kfBuYtECdie7BJFI5wOBNi05C57qV9jT_VbU8VBZ8yTy79fyBFso_3DY2nXLd6rq'
	url = 'https://fcm.googleapis.com/fcm/send'

	headers = {
		'Authorization': 'key=' + api_key,
		'Content-Type': 'application/json'
	}

	device_token = token
	payload = {
		'to' : token,
		'notification' : {
			"title": chatroom_id,
			"body": message
		}
	}

	r = requests.post(url, headers = headers, json = payload)
	if r.status_code == 200:
		print "Request sent to FCM server successfully!"
		
if __name__ == '__main__': 
	app.run(host='0.0.0.0')





