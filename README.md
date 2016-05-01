# HATEOAS-RESTful-Service

In this project, we implement the business logic using **Hypermedia As The Engine Of Application State** (HATEOAS). According to Richardson's Maturity Model, this is the last level of Maturity of RESTful service. This should be the end-goal of any RESTful service. In this case, we only expose entry URI which will be permanent to client. Using Domain Application Protocol we guide the client to reach its goal using annotated Hypermedia links. For each action, Hypermedia representation tells the client what are the actions it can perform on resources and what the related resources are. This reduces the coupling between client and server. Except the entry URI, we can change any implementation on server-side any-time.

More info about the project->

1. Client is the test driver which performs various scenarios one after another.
2. Hypermedia format for this project - application/vnd-cse564-appeals+xml 
3. Most general scenario is - professor publishes the grades with comments for the class. Then student submits the appeal for regrading particular item with explanation of why he deserves more credit.

![HTTP request mapping](https://raw.githubusercontent.com/rajeshsurana/HATEOAS-RESTful-Service/master/images/HTTP_Request_Mapping.png)   

![State Transition Diagram](https://raw.githubusercontent.com/rajeshsurana/HATEOAS-RESTful-Service/master/images/State_transition_diagram.png)
