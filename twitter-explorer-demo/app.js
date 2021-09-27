'use strict';

const fs = require('fs');

let sourceData = fs.readFileSync('src/example.json');
let users = fs.readFileSync('src/users.txt', 'utf8').split('\n');
let foods = fs.readFileSync('src/food.txt', 'utf8').split('\n');
let jsonData = JSON.parse(sourceData);
let userSize = users.length
let foodsSize = foods.length
let id = 0

function range(start, end) {
    return Array(end - start + 1).fill().map((_, idx) => start + idx)
}
function toNewJson(json) {
    let newJson = {};
    for (const key in json) {
        id++;
        if (json[key] === null) {
            newJson[key] = null
        }
        if (typeof (json[key]) == "string")
            if (key.indexOf("text") >= 0) {
                newJson[key] = range(1, 10).map(() => foods[Number((Math.random() * (foodsSize-1)).toFixed(0))])
                    .join(" ")
            } else {
                newJson[key] = key.startsWith("id") ? id.toString() :
                    (key.indexOf("name") >= 0 ? users[Number((Math.random() * (userSize-1)).toFixed(0))]
                        : foods[Number((Math.random() * foodsSize).toFixed(0))])
            }
        if (typeof (json[key]) == "number")
            newJson[key] = key.startsWith("id") ? id : (Math.random() * 9000).toFixed(0)
        if (typeof (json[key]) == "object")
            newJson[key] = toNewJson(json[key]);
    }
    return newJson;
}

let dstJsonData = toNewJson(jsonData)

let destData = JSON.stringify(dstJsonData, null).replaceAll("{}", null)
fs.writeFileSync('dst/example.json', destData);