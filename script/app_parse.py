import subprocess
import shlex
from util.mu_multiprocesses import TqdmParallelExcutor
from config import *

def run_java_command(command:str):
    command_lst = shlex.split(command)
    try:
        subprocess.run(command_lst,check=True,stdout=subprocess.PIPE,stderr=subprocess.PIPE)
    except subprocess.CalledProcessError as e:
        pass
    return
    

def single_app_parse():
    command = f"java -jar {FLOWDROID_JAR_PATH} {ANDROID_PLATFORM_PATH} {OUTPUT_DIR} {REDIS_HOST} {REDIS_PORT}"
    result = run_java_command(command)
    return result


def muti_app_parse(n_process:int=10):
    exector = TqdmParallelExcutor()
    exector.initialize(n_process=n_process)
    exector.execute(func=single_app_parse,chunksize=1)
    return 0

if __name__=="__main__":
    apk_path = ""
    out_path = ""
    command = f"java -jar {FLOWDROID_JAR_PATH} {ANDROID_PLATFORM_PATH} {OUTPUT_DIR} {REDIS_HOST} {REDIS_PORT}"
    result = run_java_command(command)