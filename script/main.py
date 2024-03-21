from app_parse import muti_app_parse
from util.file_io import get_file_list
from util.my_redis import push_file_list_to_redis
from app_parse import muti_app_parse
from config import APK_DIR

if __name__=="__main__":
    apk_lst = get_file_list(APK_DIR,endwith="apk")
    push_file_list_to_redis(apk_lst)
    muti_app_parse(n_process=10)
    