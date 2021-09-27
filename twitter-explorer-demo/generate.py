import json
import os
from pathlib import Path

import numpy

with open('src/example.json') as f:
    data = json.load(f)

Path("dst").mkdir(parents=True, exist_ok=True)


def parse_data(source):
    ret = "{"
    for j in source:
        if str(source[j]) == "False":
            ret = ret + f"'{j}':false,{os.linesep}"
        elif str(source[j]) == "True":
            ret = ret + f"'{j}':true,{os.linesep}"
        elif str(source[j]) == "None":
            ret = ret + f"'{j}':null,{os.linesep}"
        elif type(data_frame[j]) is list:
            print("")
        elif type(data_frame[j][0]) is numpy.int64 or type(data_frame[j][0]) is int:
            ret = ret + f"'{j}':{str(source[j])},{os.linesep}"
        elif type(data_frame[j][0]) is str:
            ret = ret + f"'{j}':'{str(source[j])}',{os.linesep}"
        else:
            print("@@@@@@")
            ret = ret + f"'{j}':'{parse_data(source[j])}',{os.linesep}"
    ret += "}"
    return ret


ret = parse_data(data)
print(ret)
# pprint.pprint(ret, width=20)          # here it will be wrapped exactly as expected

with open('dst/example.json', 'w') as json_file:
    json.dump(ret, json_file, indent=4)
