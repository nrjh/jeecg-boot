const twoTemp = {} // 当前重复的值,支持多列
// 值，dataSource，值,索引
// const twoMergeCellKey = (text, array, columns,index) => {
//   console.log(text, array, columns,index)
//   let i = 0
//   if (text !== twoTemp[columns]) {
//     twoTemp[columns] = text
//     array.forEach((item,j) => {
//       if (item[columns] === twoTemp[columns]) {
//         i += 1
//       }else {
//         if(j > index){
//           return 0
//         }
//         else{
//           const k = i;
//           i = 0
//           return k
//         }
//       }
//     })
//   }
//   // return i
// }

const twoMergeCellKey = (text, array, columns,index) => {
  console.log(text, array, columns,index)
  let i = 0
  if (text !== twoTemp[columns]) {
    twoTemp[columns] = text
    array.forEach((item,j) => {
      if (item[columns] === twoTemp[columns]) {
        i += 1
      }else {
        if(j > index){
          return 0
        }
        else{
          const k = i;
          i = 0
          return k
        }
      }
    })
  }
  // return i
}



export { twoMergeCellKey }