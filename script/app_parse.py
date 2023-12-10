import subprocess
import shlex
import threading
from util.my_logging import FileLogger
from util.mu_multiprocesses import TqdmParallelExcutor
from config import ANDROID_PLATFORM_PATH,FLOWDROID_JAR_PATH
logger = FileLogger(log_file="./log/parse.log",log_name="parse",log_level="INFO")

def run_java_command(command:str,timeout_secs:int=600):
    command_lst = shlex.split(command)
    apk_path = command_lst[3]
    apk_name = apk_path.split("/")[-1]
    result = None
    def run_command():
        nonlocal command_lst,result
        result = subprocess.run(command_lst,stdout=subprocess.PIPE,stderr=subprocess.PIPE)
    
    thread = threading.Thread(target=run_command)
    thread.start()
    thread.join(timeout_secs)
    if thread.is_alive():
        thread._stop()
        logger.error(f"apk {apk_name} time out")
    if result == None or result.returncode != 0:
        logger.error(f"apk {apk_name} parsing failed")
    elif result.returncode == 0:
        logger.info(f"apk {apk_name} parsing successfully")
    else:
        logger.warning(f"apk {apk_name} hit some unknow errors")
    return result

def single_app_parse(apk_path:str,out_path:str,time_out:int=600):
    command = f"java -jar {FLOWDROID_JAR_PATH} {apk_path} {ANDROID_PLATFORM_PATH} {out_path}"
    result = run_java_command(command,timeout_secs=time_out)
    return result

def single_app_parse_wapper(args):
    apk_path = args[0]
    out_path = args[1]
    return single_app_parse(apk_path,out_path)

def muti_app_parse(apk_path_lst:list,out_path:str,n_process:int=10,time_out=600):
    exector = TqdmParallelExcutor()
    exector.initialize(n_process=n_process)
    input_list = [
        (apk_path,out_path,time_out) for apk_path in apk_path_lst
    ]
    exector.execute(func=single_app_parse_wapper,input_list=input_list,chunksize=1)
    return 0

if __name__=="__main__":
    apk_path = ""
    out_path = ""
    command = f"java -jar {FLOWDROID_JAR_PATH} {apk_path} {ANDROID_PLATFORM_PATH} {out_path}"
    result = run_java_command(command)
    if result.returncode == 0:
        print("sucesss")
    else:
        print(result.stderr.decode())
        print("errors")