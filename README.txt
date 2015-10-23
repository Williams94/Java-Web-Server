README file for Practical 3 of CS5001 created by Ross Williams

Running and using the program:
To run the program you first open eclipse and import the project.
Once the project has been succesfully imported, you then open up the ServerMain class and hit the green play button to run it.
Now it will listen for clients on port 12345 by deafult (to change this look in the Configuration class and change port number).
Next you can open up a browser and type in http://localhost:12345/index.html and it will load the HTML file.
If you click on the other links in the web page it will show you that Images (PNG, GIF, JPEG) are all being retrieved and sent to the client by the server.
Try typing in http://localhost:12345/index.blah and it will present you with a 404 not found custom error page.
If you wish to see logs you can open the logs directory in eclipse and click on either access.txt to see all the succesful requests or error.txt to see all the unsucessful requests made to the server.
Multithreading has been impletemented by creating a class that extends runnable and then creating a fixedthreadpool to handle the threads that are made when this class is invoked.

The Chrome Issue:
For some reason when using file.length() to set the Content-Length header feild chrome doesnt like this and wont accept it.
However Firefox is fine with this ( the server hasnt been tested in any other browser apart from Chrome and Firefox as these are the main two).
So to get round this problem I found out that the number of lines in a file were being added twice to the file length and Chrome is not happy with this,
so to counter the problem i created a method to count the number of lines in a file and then take away the number of lines twice from the file length that was being put in the Content-Length header feild.
I also have to check if the request is coming in from Chrome to determine whether to take off the number of lines twice.
