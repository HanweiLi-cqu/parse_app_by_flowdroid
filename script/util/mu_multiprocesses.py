from tqdm.contrib.concurrent import process_map
from multiprocessing import Manager
from concurrent.futures import ProcessPoolExecutor,wait
from abc import ABC, abstractmethod
from typing import Iterable
from tqdm import tqdm
class ParallelExcutorFactory(ABC):
    def __init__(self) -> None:
        self._pool = None
    
    @abstractmethod
    def initialize(self,n_process,out_dir=None):
        pass

    @abstractmethod
    def shutdown(self):
        pass

    @abstractmethod
    def execute(self,func,input_list:Iterable,chunksize:int=1):
        pass

class TqdmParallelExcutor(ParallelExcutorFactory):
    def __init__(self) -> None:
        self.result = []

    def initialize(self,n_process:int=10):
        self.max_workers = n_process

    def shutdown(self):
        pass

    def execute(self,func,input_list:Iterable,chunksize:int=1):
        self.result = process_map(func,input_list,max_workers=self.max_workers,chunksize=chunksize)
        return self.result

class MyParallelExcutor(ParallelExcutorFactory):
    def __init__(self) -> None:
        super().__init__()
        self.result_queue = Manager().Queue()
        self.result = []
    
    def initialize(self, n_process, out_dir=None):
        self.max_workers = n_process
    def shutdown(self):
        pass
    def execute(self, func, input_list: Iterable, batch_size: int = 1):
        futures = []
        with ProcessPoolExecutor(max_workers=self.max_workers) as executor:
            for input in input_list:
                futures.append(executor.submit(func, input))
            progress_bar = tqdm(total=len(futures))
            while len(futures) > 0:
                done, _ = wait(futures, timeout=1.0)
                if len(done)>batch_size or len(futures)<=batch_size:
                    for future in done:
                        self.result_queue.put(future.result())
                        futures.remove(future)
                    progress_bar.update(len(done))
                # get result every batch_size
                # while not self.result_queue.empty():
                #     self.result.append(self.result_queue.get())
            progress_bar.close()
            while not self.result_queue.empty():
                self.result.append(self.result_queue.get())
        return self.result