import logging.config
import logging

class FileLogger:
    def __init__(self,log_file:str,log_name:str,log_level:str="DEBUG") -> None:
        self.log_file = log_file
        self.log_name = log_name
        self.log_level = log_level
        self.logger = self.get_logger()
    
    def get_logger(self):
        logger = logging.getLogger(self.log_name)
        logger.setLevel(self.log_level)
        handler = logging.FileHandler(self.log_file,encoding="utf-8",mode="a")
        handler.setLevel(self.log_level)
        formatter = logging.Formatter("%(asctime)s %(name)s %(levelname)s %(message)s")
        handler.setFormatter(formatter)
        logger.addHandler(handler)
        return logger
    
    def debug(self,msg):
        self.logger.debug(msg)
    
    def info(self,msg):
        self.logger.info(msg)
    
    def warning(self,msg):
        self.logger.warning(msg)
    
    def error(self,msg):
        self.logger.error(msg)
    
    def critical(self,msg):
        self.logger.critical(msg)
    
    def exception(self,msg):
        self.logger.exception(msg)

if __name__=="__main__":
    logger = FileLogger(log_file="../log/download.log",log_name="test",log_level="INFO")
    logger.info("xss")