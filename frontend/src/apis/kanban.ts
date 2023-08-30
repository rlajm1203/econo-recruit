import { https } from "@/src/functions/axios";
import {
  KanbanCardData,
  KanbanColumnData,
} from "../stores/kanban/Kanban.atoms";

export interface KanbanCardReq {
  id: string;
  boardId: number;
  nextBoardId: number;
  cardId: number;
  cardType: "WORK_CARD" | "APPLICANT";
  title: string;
  content: string;
  labelCount: number;
  commentCount: number;
}

export const getKanbanCards = async (columnId: string) => {
  const { data } = await https.get<KanbanCardReq[]>(
    `/boards/navigation/${columnId}`
  );

  return data;
};

interface KanbanNavigationReq {
  columnsId: number;
  title: string;
  navigationId: number;
}

export const getColums = async (navigationId: string) => {
  const { data } = await https.get<KanbanNavigationReq[]>(
    `/boards/navigation/${navigationId}/columns`
  );

  return data;
};

export const getAllKanbanData = async (
  navigationId: string
): Promise<KanbanColumnData[]> => {
  const columnsData = await getColums(navigationId);
  const cardsData = await Promise.all(
    columnsData.map((column) => getKanbanCards(column.columnsId.toString()))
  );

  return columnsData.map((column, index) => ({
    id: column.columnsId,
    title: column.title,
    card: cardsData.map((card) =>
      card[index]
        ? {
            id: card[index].cardId,
            major: card[index].title,
            title: card[index].content,
            apply: [] as string[],
            comment: card[index].commentCount,
            heart: card[index].labelCount,
            isHearted: false,
          }
        : null
    ),
  }));
};