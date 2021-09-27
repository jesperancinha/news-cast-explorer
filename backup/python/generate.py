import json
import os
from pathlib import Path

## This doesn't work!
import numpy
import pandas as pd

with open('src/example.json') as f:
    data = json.load(f)

Path("dst").mkdir(parents=True, exist_ok=True)

df = pd.DataFrame(data)


def parse_data(source, data_frame):
    ret = "{"
    for j in source:
        print(source[j])
        print(type(data_frame[j]))
        print("#####")
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
            ret = ret + f"'{j}':'{parse_data(source[j],  data_frame[j])}',{os.linesep}"
    ret += "}"
    return ret


ret = parse_data(data, df)
print(ret)
# pprint.pprint(ret, width=20)          # here it will be wrapped exactly as expected

with open('../../twitter-explorer-demo/dst/example.json', 'w') as json_file:
    json.dump(ret, json_file, indent=4)
