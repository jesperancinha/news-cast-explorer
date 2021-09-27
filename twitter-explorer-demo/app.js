'use strict';

const fs = require('fs');

let sourceData = fs.readFileSync('src/example.json');
let jsonData = JSON.parse(sourceData);

let id = 0
function toNewJson(json) {
    let newJson = {};
    id++;
    for (const key in json) {
        if (json[key] === null){
            newJson[key] = null
        }
        if (typeof (json[key]) == "string")
            newJson[key] = key.startsWith("id") ? id.toString(): "OOOOOO"
        if (typeof (json[key]) == "number")
            newJson[key] = key.startsWith("id") ? id: (Math.random() * 9000).toFixed(0)
        if (typeof (json[key]) == "object")
            newJson[key] = toNewJson(json[key]);
    }
    return newJson;
}

let dstJsonData = toNewJson(jsonData)

let destData = JSON.stringify(dstJsonData,null).replaceAll("{}", null)
fs.writeFileSync('dst/example.json', destData);