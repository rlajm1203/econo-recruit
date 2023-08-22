export const dateSplicer = (
  startDate: Date,
  endDate: Date,
  seperate: number
) => {
  const dateArray = [];
  let currentDate = new Date(startDate);
  while (currentDate <= endDate) {
    dateArray.push(new Date(currentDate));
    currentDate.setMinutes(currentDate.getMinutes() + seperate);
  }
  return dateArray;
};
