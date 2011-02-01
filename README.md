# The Garden Droid, a self monitoring and reporting mini greenhouse.

## OVERVIEW
Garden Droid consists of 3 seperate applications running together to produce the Garden Droid mini greenhouse tool. The first application is the Code which powers the Arduino Hardware and is responsible for collecting and reporting the sensor data back to a supporting computer or server. The next application is the Middleware deamon which monitors the USB/RF interface and processes the data logged from the Hardware and records it in the MySQL database. The final application is the web application which is the primary interface for users and what you'll interact with more often then not. Check out my posts on my blog for pictures and further less code based details <http://lees2bytes.blogspot.com/>.

## FEATURES

- Wireless communication
- Scheduled functionality such as grow lights, watering etc..
- PC receives Droids data for logging to a MySQL db
- Rich web app UI providing
--Reporting (custom and canned)
--Monitoring
--Email notifications
--Plant reference library.
--Planting specific observational data

## SYSTEM REQUIREMENTS

- [Java 5](http://java.com) 
- [Play Framework](http://www.playframework.org)
- [MySQL 5](http://mysql.com/)
- A public web server. (Or a machine that can run on your home network as a local server, internet viaability is not required.)
- [Arduino](http://arduino.cc/) Hardware
- Basic electronics tools and skills(or a desire to Learn them)

## Further Info
Most of the information reguarding the project should be found in the wiki

# LICENSE

TheGardenDroid's source code is licensed under the
[GNU General Public License](http://www.gnu.org/licenses/gpl.html),
except for the  external libraries listed below which have their own licenses.

## EXTERNAL LIBRARIES

- [Play Framework](http://www.playframework.org) (Some code Included) 
- [jQuery](http://jquery.com/)
- [jQuery.beautytips](http://plugins.jquery.com/project/bt)
- [jQuery.dataTables](http://datatables.net/index)
- [jQuery.textarearesizer](http://plugins.jquery.com/project/TextAreaResizer)
- [jQuery.flot] (http://code.google.com/p/flot/)

