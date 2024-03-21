import json
from typing import List, Dict, Union
import os
import glob
def read_json(path):
    with open(path, 'r',encoding="utf-8") as f:
        return json.load(f)

def write_json(path, data:Union[List,Dict], mode):
    with open(path,mode=mode,encoding="utf-8") as f:
        json.dump(data, f, indent=4, ensure_ascii=False)

def read_txt(path):
    with open(path, 'r',encoding="utf-8") as f:
        return [line.strip() for line in f.readlines()]

def write_txt(path, data:Union[List,str], mode):
    with open(path,mode=mode,encoding="utf-8") as f:
        if isinstance(data, list):
            f.write("\n".join(data))
        else:
            f.write(data)

def get_file_list(source_dir,endwith:str=None)->List:
    pattern = f"{source_dir}/**/*{endwith}"
    file_list = glob.glob(pattern, recursive=True)
    return file_list