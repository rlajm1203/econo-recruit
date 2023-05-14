import { useEffect, useState } from 'react';
import { APPLICATION_REPORT } from '@/data/25/Application';
import {
  ApplicationNavbarIndexState,
  ApplicationResultDataState,
} from '@/storage/Application/Application.atom';
import { cloneDeep } from 'lodash-es';
import { useAtom, useSetAtom } from 'jotai';
import useApplicationPageControll from '@/hooks/useApplicationPageControll.hook';
import ApplicationNextbuttonComponent from '@/components/Button/ApplicationNextButton.component';

const ApplicationQuestionReport2Component = () => {
  const [appData, setAppData] = useAtom(ApplicationResultDataState);
  const data = APPLICATION_REPORT[2];
  const [onError, setOnError] = useState(false);
  const [canNext, setCanNext] = useState(false);
  const majorClassName =
    'p-4 w-full outline-none border-[1px] border-[#DBDBDB] rounded-md';

  useEffect(() => {
    setOnError(() => appData.major === '');
    if (!appData.major) {
      setCanNext(false);
    } else {
      setCanNext(true);
    }
  }, [appData]);

  return (
    <div className="flex">
      <div className="flex w-[30rem]">
        <div className="text-xl font-semibold ">2. </div>
        <div className="pl-4">
          <div className="text-xl font-semibold pb-4">{data.title} *</div>
          <div>{data.subtitle}</div>
        </div>
      </div>
      <div className="w-[30rem]">
        <div>
          <div className="pb-4">{data.value[0].title} *</div>
          <input
            className={
              onError ? '!border-[#DC0000] ' + majorClassName : majorClassName
            }
            type="text"
            value={appData.major}
            onChange={(e) =>
              setAppData((v) => {
                const cv = cloneDeep(v);
                cv.major = e.target.value;
                return cv;
              })
            }
          />
          <div className="h-4">
            {onError ? (
              <div className="w-full translate-x-[100%]">
                <div className="w-fit text-[#DC0000] -translate-x-[calc(100%+1rem)] -translate-y-10">
                  {data.value[0].title}을 입력해주세요.
                </div>
              </div>
            ) : (
              ''
            )}
          </div>
          <div>
            <div className="pb-4">{data.value[1].title}</div>
            <input
              className={majorClassName}
              type="text"
              value={appData.doubleMajor}
              onChange={(e) =>
                setAppData((v) => {
                  const cv = cloneDeep(v);
                  cv.doubleMajor = e.target.value;
                  return cv;
                })
              }
            />
            <div className="h-4"></div>
          </div>
          <div>
            <div className="pb-4">{data.value[2].title}</div>
            <input
              className={majorClassName}
              type="text"
              value={appData.minor}
              onChange={(e) =>
                setAppData((v) => {
                  const cv = cloneDeep(v);
                  cv.minor = e.target.value;
                  return cv;
                })
              }
            />
            <div className="h-4"></div>
          </div>
          <ApplicationNextbuttonComponent canNext={canNext} />
        </div>
      </div>
    </div>
  );
};

export default ApplicationQuestionReport2Component;