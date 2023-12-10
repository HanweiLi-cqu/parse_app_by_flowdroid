import json
from typing import List, Dict, Union
import os
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
    file_list = []
    for root, dirs, files in os.walk(source_dir):
        for file in files:
            if endwith is None:
                file_list.append(os.path.join(root, file))
            else:
                if file.endswith(endwith):
                    file_list.append(os.path.join(root, file))
        for dir in dirs:
            file_list.extend(get_file_list(dir,endwith=endwith))
    return file_list