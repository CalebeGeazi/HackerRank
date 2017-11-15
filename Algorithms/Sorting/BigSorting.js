/* https://www.hackerrank.com/challenges/big-sorting/problem */

process.stdin.resume();
process.stdin.setEncoding('ascii');

var input_stdin = "";
var input_stdin_array = "";
var input_currentline = 0;

process.stdin.on('data', function (data) {
    input_stdin += data;
});

process.stdin.on('end', function () {
    input_stdin_array = input_stdin.split("\n");
    main();    
});

function readLine() {
    return input_stdin_array[input_currentline++];
}

/////////////// ignore above this line ////////////////////

function main() {
    var n = parseInt(readLine());
    var unsorted = [];
    for(var unsorted_i = 0; unsorted_i < n; unsorted_i++){
       unsorted[unsorted_i] = readLine();
    }
    // your code goes here
    var sorted = unsorted.sort(numericSort);
    for (var i = 0; i < sorted.length; i++) {
        console.log(sorted[i]);
    }
}

function numericSort(a, b) {
  // add padding to strings so they are the same length
  var largerLength = a.length > b.length
    ? a.length
    : b.length;
  a = "0".repeat(largerLength - a.length) + a;
  b = "0".repeat(largerLength - b.length) + b;

  // compare each number relative to their index
  for (var i = 0; i < a.length; i++) {
    var aI = parseInt(a[i]);
    var bI = parseInt(b[i]);
    if (aI - bI < 0 || aI - bI > 0) {
      return aI - bI;
    }
  }
  return true;
}
