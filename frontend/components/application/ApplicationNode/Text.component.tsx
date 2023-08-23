"use client";

import Txt from "@/components/common/Txt.component";
import {
  ApplicationNode,
  ApplicationText,
} from "@/src/constants/application/type";
import { replacer } from "@/src/functions/replacer";
import { validator } from "@/src/functions/validator";
import { useLocalStorage } from "@/src/hooks/useLocalstorage.hook";
import classNames from "classnames";
import { FC, useId, useState } from "react";

interface ApplicationTextProps {
  data: ApplicationNode;
}

const ApplicationText: FC<ApplicationTextProps> = ({ data }) => {
  const textData = data as ApplicationText;
  const id = useId();
  const [value, setValue] = useLocalStorage(textData.name, "");
  const [isError, setIsError] = useState(false);

  return (
    <div className="relative">
      {textData.title && (
        <label>
          <Txt typography="h6">{`${textData.title}${
            textData.require ? "*" : ""
          }`}</Txt>
          {textData.subtitle && <Txt>{` ${textData.subtitle}`}</Txt>}
        </label>
      )}
      <input
        className={classNames(
          "my-2 border rounded-lg p-4 w-full",
          isError && "border-[#DC0000]"
        )}
        type="text"
        id={id}
        value={value}
        onChange={(e) => {
          const value = textData.validate
            ? replacer(e.target.value, textData.validate)
            : e.target.value;
          const isError = textData.validate
            ? !validator(value, textData.validate)
            : false;
          setIsError(isError);
          setValue(value);
        }}
        maxLength={textData.maxLength ?? 1000}
        minLength={textData.minLength ?? 0}
      />
      {isError && textData.errorMessages ? (
        <div className="absolute w-full translate-x-[100%]">
          <div className="w-fit text-[#DC0000] -translate-x-[calc(100%+1rem)] -translate-y-12">
            {textData.errorMessages}
          </div>
        </div>
      ) : (
        ""
      )}
    </div>
  );
};

export default ApplicationText;