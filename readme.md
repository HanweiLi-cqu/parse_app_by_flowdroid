## usage
### src
code for building artefact which u could download from releases. The code logic is to extract the cg using flowdroid and then use a depth-first algorithm
### script
The goal is to speed up the parsing process using multiple processes.
#### prepare
1. modify config.py: 
    - `ANDROID_PLATFORM_PATH`: a sub dir of android sdk)
    - `FLOWDROID_JAR_PATH`: download it from release or compile it yourself into a jar file based on the src code.
2. Setting the values of variables in main.py
    - `apk_dir`: the directory of apk files
    - `output_dir`: the directory of output files
    - `process_num`: the number of processes
> If you only want to work on one file at a time, just use the `app_parse/single_app_parse.py`

#### run
```bash
cd script
pip install -r requirements.txt
python main.py
```

