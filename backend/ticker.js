var StockSymbolLookup = require('stock-symbol-lookup');
const writeFile = require('fs').writeFile;
var out;
StockSymbolLookup.loadData().then((data) => {
    out = data[0];
    writeFile('./test-data.json', JSON.stringify(data.symbols[0], null, 4), (err) => {
        if(err) {
            console.log(err); 
            throw new Error(err);
        }
        console.log('Success!');
    });
    console.log(out);
    });



