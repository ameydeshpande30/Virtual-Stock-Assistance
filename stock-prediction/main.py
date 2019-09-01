from flask import jsonify
import requests,json

#import matplotlib.pyplot as plt
#from matplotlib import style
from datetime import datetime
from flask import Flask
import pandas as pd
from statsmodels.tsa.arima_model import ARIMAResults


app = Flask(__name__)

resNflx = ARIMAResults.load('./models/ntflx_sarima.pkl')
resAMZ = ARIMAResults.load('./models/amzn_sarima.pkl')
resGOOGL = ARIMAResults.load('./models/google_sarima.pkl')

@app.route("/<string:name>/")
def index(name):
    date = datetime.today().strftime('%Y-%m-%d')
    if name == "ntflx":
        pred = resNflx.get_prediction(end=pd.to_datetime(date),dynamic=False)
    elif name == "amazon":
        pred = resAMZ.get_prediction(end=pd.to_datetime(date),dynamic=False)
    else:
        pred = resGOOGL.get_prediction(end=pd.to_datetime(date),dynamic=False)
    ans = pred.predicted_mean[-1]
    c = pred.predicted_mean[-2]
    pre = pred.predicted_mean[-3]
    return jsonify(
        prev = pre,
        curr = c,
        fd = ans
    )

def json_list(list):
    lst = []
    for pn in list:
        d = {}
        d['mpn']=pn
        lst.append(d)
    return json.dumps(lst)

@app.route("/noun/<string:statement>/")
def state(statement):
	url = 'https://api.aylien.com/api/v1/elsa'
	payload = {'text': 'i like to buy google stock'}
	headers = {'X-AYLIEN-TextAPI-Application-Key': '3f5f0bc779ca61a2801894707ee82e08', 'X-AYLIEN-TextAPI-Application-ID': 'f91c6ba2', 'Content-Type': 'application/json'}

	r = requests.post(url, data=json.dumps(payload), headers=headers)
	print(r)
	return (r.json)

if __name__ == "__main__":
    app.run(host = '0.0.0.0', port = 5000, debug = True)
