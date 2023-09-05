import { DropResult } from "@hello-pangea/dnd";
import { KanbanColumnData } from "@/src/stores/kanban/Kanban.atoms";

export const getMovedKanbanData = (
  kanbanData: KanbanColumnData[],
  result: DropResult
): KanbanColumnData[] => {
  if (!result.destination) return kanbanData;

  if (result.type === "COLUMN") {
    const fromIndex = result.source.index;
    const toIndex = result.destination.index;

    if (toIndex === fromIndex) return kanbanData;

    const shallow = structuredClone(kanbanData);
    const pickData = shallow.splice(fromIndex, 1);
    shallow.splice(toIndex, 0, ...pickData);

    return shallow;
  }

  if (result.type === "DEFAULT") {
    const from = result.source;
    const to = result.destination;

    const shallow = structuredClone(kanbanData);
    const pickData = shallow[+from.droppableId].card.splice(from.index, 1);
    shallow[+to.droppableId].card.splice(to.index, 0, ...pickData);

    return shallow;
  }

  return kanbanData;
};

export const getFromToIndexColumn = (
  kanbanData: KanbanColumnData[],
  result: DropResult
): { columnId: number; targetColumnId: number } => {
  if (!result.destination) return { columnId: 0, targetColumnId: 0 };

  const fromIndex = result.source.index;
  const toIndex = result.destination.index;

  const columnId = kanbanData[+fromIndex - 1].id ?? kanbanData[0].id ?? 0;
  const targetColumnId = kanbanData[+toIndex - 1].id ?? kanbanData[0].id ?? 0;

  return { columnId, targetColumnId };
};

export const getFromToIndexDefault = (
  kanbanData: KanbanColumnData[],
  result: DropResult
): { boardId: number; targetBoardId: number } => {
  if (!result.destination) return { boardId: 0, targetBoardId: 0 };

  const from = result.source;
  const to = result.destination;

  const boardId =
    kanbanData[+from.droppableId - 1].card[from.index]?.id ??
    kanbanData[+from.droppableId - 1].card[0]?.id ??
    0;
  const targetBoardId =
    kanbanData[+to.droppableId - 1].card.length === 1
      ? kanbanData[+to.droppableId - 1].card[to.index]?.id ?? 0
      : kanbanData[+to.droppableId - 1].card[to.index - 1]?.id ??
        kanbanData[+from.droppableId - 1].card[0]?.id ??
        0;

  return { boardId, targetBoardId };
};