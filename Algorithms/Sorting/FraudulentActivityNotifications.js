/* https://www.hackerrank.com/challenges/fraudulent-activity-notifications/problem */

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

function main() {
    var n_temp = readLine().split(' ');
    var n = parseInt(n_temp[0]);
    var d = parseInt(n_temp[1]);
    expenditure = readLine().split(' ');
    expenditure = expenditure.map(Number);
    var result = activityNotifications(expenditure, d);
    process.stdout.write("" + result + "\n");    
}

/////////////// ignore above this line ////////////////////

function activityNotifications(ex, d) {
    // Complete this function
    var n = 0;
    
    // keep copy of already sorted values
    var sortedEx = [];
    
    // loop thru expenditures starting at index d, since there's 
    // no need to check for fradulent activity before d days
    for (var i = d; i < ex.length - 1; i++) {
        // first time thru
        if (i == d) {
            // sort the first d days
            sortedEx = ex.slice((i - d),(i)).sort(sort);
            // check if the current value is over the threshold
            n = n + isOverThreshold(ex[i], sortedEx);
        }
        
        // every other time thru
        else {
            // remove the first element in ex[] from the sortedEx    
            sortedEx.splice(sortedEx.indexOf(ex[i-d-1]), 1);
            // use a binary insertion sort to add previous value to 
            // sorted array
            binaryInsertionSort(sortedEx, ex[i-1]);
            // check if the current value is over the threshold
            n = n + isOverThreshold(ex[i], sortedEx);
        }
    }
    
    return n;
}

// funtion that is given a sorted array and a value, inserts the value 
// at the correct index in the sorted array to maintain the array sorted.
function binaryInsertionSort(sortedArr, val) {
    // get the index in which to insert the val
    var index = findInsertionPoint(sortedArr, val);
    // return new array with inserted val
    return sortedArr.splice(index, 0, val);
}

// recusive function that is given an array, a value, a min and max index 
// - returns the index in which the val should be inserted to 
function findInsertionPoint(arr, val, min = 0, max = arr.length - 1) {
    // if the difference of max and min is 1, then we can derive where the 
    // value should be inserted
    if (max - min == 1) {
        return val >= arr[max] ? max + 1 : max;
    }
    
    // get the middle index 
    var mI = Math.floor((max-min)/2) + min;
    
    // reset min and max accordingly
    if (val > arr[mI]) {
        min = mI;
    } 
    else if (val < arr[mI]) {
        max = mI;
    } 
    // or just return the middle index as the index if val == arr[mI]
    else {
        return mI;
    }
    
    // recursive call with updated min/max
    return findInsertionPoint(arr, val, min, max);
}

// function to check if the val is over the fradulent threshold
function isOverThreshold(val, sortedEx) {
    return val >= (2 * findMedian(sortedEx)) ? 1 : 0;
}

// function to so a numeric sort
function sort(a, b) {
    return a - b;
}

// function to find the median of a given array
function findMedian(arr) {    
    return arr.length % 2 
        ? arr[Math.floor((arr.length/2))]
        : (arr[Math.floor((arr.length/2))] + arr[Math.floor((arr.length/2)) - 1])/2
}
