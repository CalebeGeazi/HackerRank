// function given a number n representing the nth row of Pascal's Triangle, 
// prints out the values for the row.
// Inspired by https://en.wikipedia.org/wiki/Pascal%27s_triangle#Calculating_a_row_or_diagonal_by_itself
function pascalsTriangleNthRow(n) {
  // every row starts with 1
  var row = [1];
  
  // interate n/2 times to calculate first half of the row
  for (var i = n; i > Math.ceil(n/2); i--) {
    // according to the wiki article, we can find the next number in the row
    // by multiplying the last number by the fraction ((n-(n-i))/(n-i+1))
    var nextNum = row[row.length-1] * ((n-(n-i))/(n-i+1));
    
    // add number to row
    row.push(nextNum);
  }
  
  // determine 2nd half of row based on if n is odd or even.
  // if even, then the middle two numbers are the same so we can just
  // duplicate the current row in reverse order.
  if (n % 2) {
    row = row.concat(row.slice(0,row.length).reverse());
  } 
  // if odd, then duplicate the row in reverse order, but exclude the 
  // last number
  else {
    row = row.concat(row.slice(0,row.length-1).reverse());
  }
  
  // print the row to the console.
  console.log(row.toString());
}
