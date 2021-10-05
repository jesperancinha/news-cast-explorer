'use strict';

import fs from 'fs'
import { format, formatDistance, formatRelative, subDays } from 'date-fns'

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

function createRandomNumber() {
    return Number((Math.random() * 9000).toFixed(0));
}

function toNewJson(json) {
    let newJson = {};
    id++;
    for (const key in json) {
        if (Array.isArray(json[key]))
            newJson[key] = json[key].map(entry => {
                if (typeof (entry) == "object")
                    return toNewJson(entry)
                else if (typeof (entry) == "number")
                    return createRandomNumber()
                else if (typeof (entry) == "string")
                    return range(1, 10).map(() => foods[Number((Math.random() * (foodsSize - 1)).toFixed(0))])
                        .join(" ")
                else return entry
            })
        else if (json[key] === null) {
            newJson[key] = null
        } else if (typeof (json[key]) == "string") {
            if (key.indexOf("timestamp") >= 0) {
                newJson[key] = (Math.random() * new Date().getTime()).toFixed(0)
            } else if (key.indexOf("created") >= 0) {
                newJson[key] = format(new Date(), "EEE MMM dd HH:mm:ss +0000 yyyy");
            } else if (key.indexOf("text") >= 0) {
                newJson[key] = range(1, 10).map(() => foods[Number((Math.random() * (foodsSize - 1)).toFixed(0))])
                    .join(" ")
            } else {
                newJson[key] = key.startsWith("id") ? id.toString() :
                    (key.indexOf("name") >= 0 ? users[Number((Math.random() * (userSize - 1)).toFixed(0))]
                        : foods[Number((Math.random() * foodsSize).toFixed(0))])
            }

        } else if (typeof (json[key]) == "number")
            newJson[key] = key.startsWith("id") ? id : createRandomNumber()
        else if (typeof (json[key]) == "object")
            newJson[key] = toNewJson(json[key]);
        else if (typeof (json[key]) == "boolean")
            newJson[key] = json[key]
    }

    return newJson;
}


let dstJsonData = toNewJson(jsonData)
let destData = JSON.stringify(dstJsonData, null).replaceAll("{}", null)
fs.writeFileSync('dst/example.json', destData);

for (let i = 1; i <= 15; i++) {
    let dstJsonData = toNewJson(jsonData)
    let destData = JSON.stringify(dstJsonData, null).replaceAll("{}", null)
    fs.writeFileSync('dst/example' + i + '.json', destData);
}
