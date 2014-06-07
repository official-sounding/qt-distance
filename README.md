# QT Stats

This is a little toy that loads the data from QuikTrip's [location finder](http://www.quiktrip.com/Locations) and tries to find the point in tulsa that is farthest away from a quik trip

I make no claims to its accuracy, precision or correctness.  Not suggested for use in surveying, planning or house purchasing.

Code is licensed under the MIT License

## Theory of operation
after getting the list of gas stations (which helpfully includes lat/long) within 5 miles, 
I build a bounding box that roughly encloses the defined points.
I iterate over each triple of points, and determine the size of the circle enclosed by those three points.
If the midpoint falls inside the box, and no other QuikTrips fall inside the box, and the radius the largest encountered so far,
this is the largest empty circle, and it's midpoint is the point farthest away from any QuikTrip in Tulsa.

## Results

The farthest point away from any quiktrip, based on the (somewhat arbitrary) rules I set up is
[2151 S Peoria Ave](https://www.google.com/maps/place/2151+S+Peoria+Ave/@36.1315582,-95.9756482,17z/data=!3m1!4b1!4m2!3m1!1s0x87b6eca3a681717f:0xd8c6e83ed7013624).
This point is 1.15 miles away from the QuikTrips on 15th & Denver, 11th & Utica and 36th & Peoria.  

## Next Steps
I can probably give this a bit more rigor by using the data for 10 miles within 74135, then using some bounds 
(probably something like US 75, US 169, Creek Turnpike, Independence St), 
or use the actual boundaries of the city of Tulsa and use a convex hull as my boundary.
Also, this could probably use a Voronoi diagram and get a pretty picture to boot.


## MIT License

The MIT License (MIT)

Copyright (c) 2014 Peter Elliott

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
