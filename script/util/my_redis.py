# Redis utils类，用于push文件列表到redis队列中
from redis import Redis
from config import REDIS_HOST, REDIS_PORT,REDIS_KEY
from typing import List

def push_file_list_to_redis(file_list:List):
    client = Redis(host=REDIS_HOST, port=REDIS_PORT, db=0)
    for file in file_list:
        client.sadd(REDIS_KEY, file)
    return