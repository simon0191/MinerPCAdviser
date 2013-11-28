var express = require('express');
var SortedSet = require("collections/sorted-set");
var rest = require('restler');
var fs = require('fs');

var products;
var filters;

var getFilters = function(req, res) {	
	res.send(200,{'filters':filters});
}

var getProducts = function(req, res) {
	res.send(200,{'products':products});
}
var getFilteredProducts = function(req, res) {
	//console.log(err);
	try {
		console.log(req.body);
		var actFilters = req.body.filters;


		/*
		private SortedMap<MpcaProduct,Boolean> filter(SortedMap<MpcaProduct,Boolean> ps,List<MpcaFilter> fs) {
		Set<MpcaProduct> keys = ps.keySet();
		
		for(MpcaProduct p:keys) {
			ps.put(p, false);
			int nPassedFilters = 0;
			for(MpcaFilter f:fs) {
				Set<Map.Entry<Comparable,Boolean>> entrySet = f.getValues().entrySet();
				for (Map.Entry<Comparable, Boolean> entry : entrySet) {
					if(entry.getValue() && p.getProperty(f.getName()).equals(entry.getKey())) {
						nPassedFilters++;
						break;
					}
				}
			}
			if(nPassedFilters == fs.size()) {
				ps.put(p, true);
			}
		}
		return ps;
	}
		*/
		var filteredPs = [];
		products.forEach(function(p){
			var add = false;
			var nPassedFilters = 0;
			actFilters.forEach(function(f){
				f.elems.forEach(function(e){
					if(p[f.name] == e) {
						nPassedFilters++;
						return;
					}
				});
				
			});
			if(nPassedFilters == actFilters.length) {
				filteredPs.push(p);
			}
		});
		res.send(200,{products:filteredPs});
	}
	catch(err) {
		console.log(err);
		res.send(400,{errors:['Bad request']});
		return;		
	}
}

var loadData = function() {
	var data = fs.readFileSync('data/db.csv','utf-8');
	products = [];

	var lines = data.toString().split('\n');
	var header = lines[0].split('\t');

	var type = lines[1].split('\t');
	lines = lines.splice(2);

    lines.forEach(function(line){
        var t = line.split('\t');
        if(t.length == 11) {
            var p = {};
            header.forEach(function(h,i) {
            	switch(type[i]) {
            		case 'integer':
            			p[h] = parseInt(t[i]);
            		break;
            		case 'float':
            			p[h] = parseFloat(t[i]);
            		break;
            		case 'string':
            			p[h] = t[i];
            		break;
            	}
            });
            products.push(p);
        }
    });
    console.log('Size: '+products.length);
	/*
	var request = rest.get('https://raw.github.com/simon0191/MinerPCAdviser/develop/MPCA_REST_API/data/db.csv');
	request.on('complete', function(data) { });
	*/

}
var createFilters = function() {
	var brandSet = new SortedSet();
	var ramSet = new SortedSet();
	var hdSet = new SortedSet();
	products.forEach(function(p) {
		brandSet.add(p['brand']);
		ramSet.add(p['ram']);
		hdSet.add(p['hd']);
	});
	filters = [
		{name:'brand'       ,type:'String',elems:brandSet.toArray()},
		{name:'ram'         ,type:'Number',elems:ramSet.toArray()},
		{name:'hd'  ,type:'Number',elems:hdSet.toArray()}
	];
}
var searchProductById = function(id) {
	var ret;
	products.forEach(function(p){
		if(p.id == id) {
			ret = p;
			return;
		}
	});
	return ret;
}
var getWordCloud = function(req,res) {
	try{
		var id = req.query.id;
		var p = searchProductById(id);
		res.send(200,{url:p.wordcloud});
	}catch(err) {
		res.send(400,'Bad request');
	}
}

var test = function(req, res) {
	var request = rest.get('https://dl.dropboxusercontent.com/u/20243104/db.csv');
	request.on('complete', function(result) { 
		res.send(200,result);
	});

}
var postTest = function(req, res) {
	console.log(req.body);
	console.log(req.body.filters[0].elems);
	res.send(200,{res:'ok'});
}
var launchWebApp = function() {
	var app = express();
	app.use(express.bodyParser());

	app.get('/',function(req,res){
		res.send(200,{msg:'Hello JSON'});
	});

	app.get('/filters',getFilters);
	app.get('/products',getProducts);
	app.post('/products',getFilteredProducts);
	app.get('/products/wordcloud',getWordCloud);

	//Test
	app.get('/test',test);
	app.post('/postTest',postTest)
	//tseT

	var port = process.env.PORT || 8080;
	app.listen(port,function() {
		console.log('Listening on port: '+port);
	});

}

var main = function() {
	loadData();
	createFilters();
	launchWebApp();
}

main();