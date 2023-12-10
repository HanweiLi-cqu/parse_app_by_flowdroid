from app_parse import muti_app_parse
from util.file_io import get_file_list
if __name__=="__main__":
    apk_dir = ""
    apk_lst = get_file_list(apk_dir,endwith=".apk")
    out_dir = ""
    muti_app_parse(apk_lst, out_dir,n_process=10)