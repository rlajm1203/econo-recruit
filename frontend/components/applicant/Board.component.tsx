"use client";

import Board from "@/components/common/board/Board.component";
import {
  getAllApplicant,
  getApplicantByIdWithField,
  getApplicantByPage,
} from "@/src/apis/applicant/applicant";
import ApplicantDetailRight from "./DetailRight.component";
import ApplicantDetailLeft from "./DetailLeft.component";
import { FC, useState } from "react";
import { ApplicantReq } from "@/src/apis/application";
import { applicantDataFinder } from "@/src/functions/finder";
import { useQuery } from "@tanstack/react-query";
import { useSearchParams } from "next/navigation";

interface ApplicantBoardProps {
  generation: string;
}

const ApplicantBoard: FC<ApplicantBoardProps> = ({ generation }) => {
  const [data, setData] = useState<ApplicantReq[]>([]);
  const searchParams = useSearchParams();
  const pageIndex = searchParams.get("page") || "1";

  const onClick = (id: string) => {
    if (!allData) return;
    setData(
      applicants?.filter((value) => applicantDataFinder(value, "id") === id)[0]
    );
  };

  const {
    data: allData,
    isLoading,
    isError,
  } = useQuery(
    ["allApplicant", generation],
    () => getApplicantByPage(+pageIndex),
    {
      enabled: !!generation,
    }
  );

  if (!allData || isLoading) {
    return <div>로딩중...</div>;
  }

  if (isError) {
    return <div>에러 발생</div>;
  }

  const { maxPage, applicants } = allData;

  const boardData = applicants.map((value) => ({
    id: applicantDataFinder(value, "id"),
    title: `[${applicantDataFinder(value, "field")}] ${applicantDataFinder(
      value,
      "name"
    )}`,
    subElements: [
      applicantDataFinder(value, "field1"),
      applicantDataFinder(value, "field2"),
      `${applicantDataFinder(value, "grade")} ${applicantDataFinder(
        value,
        "semester"
      )}`,
      applicantDataFinder(value, "uploadDate") === ""
        ? new Date().toLocaleString("ko-KR", { dateStyle: "short" })
        : new Date(
            Number(applicantDataFinder(value, "uploadDate"))
          ).toLocaleString("ko-KR", { dateStyle: "short" }),
    ],
  }));

  return (
    <Board wapperClassname="divide-x" boardData={boardData} onClick={onClick}>
      <div className="flex flex-1">
        <div className="flex-1 overflow-auto px-12 min-w-[40rem]">
          <ApplicantDetailLeft data={data} />
        </div>
      </div>
      <div className="flex flex-1 min-h-0">
        <div className="flex-1 overflow-auto px-12">
          <ApplicantDetailRight data={data} />
        </div>
      </div>
    </Board>
  );
};

export default ApplicantBoard;
