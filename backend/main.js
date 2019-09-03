const express = require('express')
const fs = require('fs');
const axios = require('axios')
port = process.env.PORT || 3000 ;
const Fuse = require('fuse.js');
var cors = require('cors')
let rawdata = fs.readFileSync('db.json');
let db = JSON.parse(rawdata);
var morgan  = require('morgan')
var Sentiment = require('sentiment');
var sentiment1 = new Sentiment();
var app = express()
app.use(morgan())
var ip = "http://192.168.43.40:5000"
var options = {
    shouldSort: true,
    threshold: 0.8,
    location: 0,
    distance: 100,
    maxPatternLength: 32,
    minMatchCharLength: 1,
    keys: [
      "securityName",
    ]
  };
  
  var fuse = new Fuse(db, options);

  app.get('/buy/:cname', async (req, res) =>{
    var result = fuse.search(req.params.cname);
    var out = result.slice(0, 1);
    // res.send(out)
    let ticker = out[0].symbol;
    console.log(ticker)
    try{
    let response = await axios.get("https://stocknewsapi.com/api/v1?tickers="+ticker+"&items=50&token=0gjocnvwhorcwggt0xx4ptzaqotqzzheankhy0jg");
    let data = response.data.data;
    data = data.slice(0,50)
    var count = 0
    for (var i = 0; i < data.length; i++) {
        if(data[i].sentiment === "Positive"){
            count+= (60-i)/60;
        }
        else if(data[i].sentiment === "Negative"){
            count-= (60-i)/60;
        }
        var result = sentiment1.analyze(data[i].title);
        if(result != 0){
            count += result.score;
        }
    }
    let future = await axios(ip + "/" + ticker);
    let fd = future.data.fd;
    let prev = future.data.prev;
    let curr = future.data.curr;
    let sentiment = (count/data.length)*100 
    let ans = fd + sentiment
    ans /= 2;
    let outD = ""
    let code = 1
    if(ans > 20){
        outD += `Stock has moved from ${prev} to ${curr} and is expected to rise by ${parseInt(ans)}% in future. So it is suggested to buy the stocks.`
    }
    else if(ans < -20){
        code = 2;
        outD += `Stock has moved from ${prev} to ${curr} and is expected to fall by ${parseInt(ans)}% in future. So it is suggested to sell the stocks.`
    }
    else{
        if(ans > 0){
            outD += `Stock has moved from ${prev} to ${curr} and is expected to rise by just ${parseInt(ans)}% in future. So it is suggested to buy or wait.`
            code = 3;
        }
        else{
            outD += `Stock has moved from ${prev} to ${curr} and is expected to fall by just ${parseInt(ans)}% in future. So it is suggested to wait.`
            code = 4;
        }

    }
    let status="";
    switch(code){
        case 2: status = "SELL";
                break;
        case 1: status = "BUY";
                break;
        case 3: status = "BUY or WAIT";
                break;
        case 4: status = "WAIT";
                break;
        default: status = "BUY";
                break;
    }
    returnValue = {
        'ticker' : ticker,
        "status" : status,
        "reason" : outD
    }
    res.send(returnValue);
}
catch(e){
    console.log(e)
    res.sendStatus(404)
}
}
)


//sell
app.get('/sell/:cname/:stocknum/:stockrate', async (req, res) =>{
    var result = fuse.search(req.params.cname);
    var snum = req.params.stocknum;
    var srate = req.params.stockrate;
    srate = parseInt(srate);
    snum = parseInt(snum);
    var out = result.slice(0, 1);
    // res.send(out)
    let ticker = out[0].symbol;
    console.log(ticker)
    
    let response = await axios.get("https://stocknewsapi.com/api/v1?tickers="+ticker+"&items=50&token=0gjocnvwhorcwggt0xx4ptzaqotqzzheankhy0jg");
    let data = response.data.data;
    data = data.slice(0,50)
    var count = 0
    for (var i = 0; i < data.length; i++) {
        
        if(data[i].sentiment === "Positive"){
            count+= (60-i)/60;
        }
        else if(data[i].sentiment === "Negative"){
            count-= (60-i)/60;
        }

    }
    let future = await axios(ip + "/" + ticker);
    let fd = future.data.fd;
    let prev = future.data.prev;
    let curr = future.data.curr;
    let sentiment = (count/data.length)*100 
    let ans = fd + sentiment
    ans /= 2;
    let outD = ""
    let code = 2
    if(ans > 20){
        /*if(srate < curr*snum){
            outD += `Stock has moved from ${prev} to ${curr} and is expected to rise by 
                ${parseInt(ans)}% in future. So it is suggested to hold the stocks.`    
        }
        else{
            outD += `Stock has moved from ${prev} to ${curr} and is expected to rise by 
                ${parseInt(ans)}% in future. So it is suggested to hold the stocks.`
        }*/
        outD += `Stock has moved from ${prev} to ${curr} and is expected to rise by ${parseInt(ans)}% in future. So it is suggested to hold the stocks.`
        code = 5;
    }
    else if(ans < -20){
        if(srate < curr*snum - 20){
            outD += `Stock has moved from ${prev} to ${curr} and is expected to fall by ${parseInt(ans)}% in future. But it is suggested to hold the stocks.`
            code = 5;
        }
        else{
            outD += `Stock has moved from ${prev} to ${curr} and is expected to fall by ${parseInt(ans)}% in future. So it is suggested to sell the stocks.`
            code = 2;
        }
    }
    else{
        if(ans > 0 && srate < curr*snum){
            outD += `Stock has moved from ${prev} to ${curr} and is expected to rise by just ${parseInt(ans)}% in future. So it is suggested to sell the stocks.`
            code = 2;
        }
        else if(ans <= 0 && srate < curr*snum){
            outD += `Stock has moved from ${prev} to ${curr} and is expected to fall by just ${parseInt(ans)}% in future. So it is suggested to sell or hold the stocks.`
            code = 6;
        }
        //srate > curr*snum
        else if(ans > 0){ 
            outD += `Stock has moved from ${prev} to ${curr} and is expected to rise by just ${parseInt(ans)}% in future. So it is suggested to hold the stocks.`
            code = 5;
        }
        else{
            outD += `Stock has moved from ${prev} to ${curr} and is expected to fall by just ${parseInt(ans)}% in future. So it is suggested to hold the stocks.`
            code = 5;
        }

    }
    let status="";
    switch(code){
        case 2: status = "SELL";
                break;
        case 5: status = "HOLD";
                break;
        case 6: status = "SELL or HOLD";
                break;
        default: status = "SELL";
                break;
    }
    returnValue = {
        'ticker' : ticker,
        "status" : status,
        "reason" : outD
    }
    res.send(returnValue);

})


app.listen(port, () => {console.log("Server is running")})