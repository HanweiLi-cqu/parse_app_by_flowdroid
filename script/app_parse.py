import subprocess
import shlex
from config import *
import multiprocessing

def run_java_command(command:str):
    command_lst = shlex.split(command)
    try:
        res=subprocess.run(command_lst,check=True,stdout=subprocess.PIPE,stderr=subprocess.STDOUT)
        print(res.stdout.decode())
    except subprocess.CalledProcessError as e:
        print(f"Error: {e}")
        pass
    return
    

def single_app_parse():
    command = f"java -jar {FLOWDROID_JAR_PATH} {ANDROID_PLATFORM_PATH} {OUTPUT_DIR} {REDIS_HOST} {REDIS_PORT} {REDIS_KEY}"
    result = run_java_command(command)
    return result


def muti_app_parse(n_process:int=10):
    processes = []
    for _ in range(n_process):
        p = multiprocessing.Process(target=single_app_parse)
        p.start()
        processes.append(p)
    for p in processes:
        p.join()
    return 0

if __name__=="__main__":
    apk_path = ""
    out_path = ""
    command = f"java -jar {FLOWDROID_JAR_PATH} {ANDROID_PLATFORM_PATH} {OUTPUT_DIR} {REDIS_HOST} {REDIS_PORT} {REDIS_KEY}"
    print(command)
    result = run_java_command(command)