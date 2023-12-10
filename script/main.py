from app_parse import muti_app_parse
from util.file_io import get_file_list

def check_apk_parsed(apk_lst:list,out_dir):
    """
    if apk not parsed, return it
    """
    out_lst = get_file_list(out_dir)
    out_parsed_apk_lst = [x.split("/")[-1].replace(".txt","") for x in out_lst]
    res = []
    for apk in apk_lst:
        apk_name = apk.split("/")[-1].replace(".apk","")
        if apk_name not in out_parsed_apk_lst:
            res.append(apk)
    return res

if __name__=="__main__":
    apk_dir = ""
    apk_lst = get_file_list(apk_dir,endwith=".apk")
    out_dir = ""
    apk_lst = check_apk_parsed(apk_lst,out_dir)
    muti_app_parse(apk_lst, out_dir,n_process=10)