# database-migration-tool
I'm thinking about refactoring the small Java project, called Database Migration tool, that I developed for a college project with Divash Adhikari and Sudeep Bhandari. The feature for this project is simple, it just reads the existing MS-Access database and migrates all of its tables and related data to another popular database of user choice for ex. MySQL. During our engineering days, it seemed too complicated and we did a lot of research on how to undertake this task and propose a solution. As a result, we thought to make this as a desktop app and make heavy use of file. The process on which we agreed and implemented would go on like this:
1. First user provides MS-Access db file whose extension is generally of mdb or accdb and configuration for MySQL to which provided MS-Access db would get migrated.
2. Then, our system first scans the table and its metadata from MS-Access and extracts and stores them in a file.
3. After this, on second pass, the system now scans the table structure(the keys associated with it, the column data type, etc) and prepares the DDL query.
4. In the same process, the system fetches data in bulk from the respective tables and prepares the DML query and both table structure and insert queries are saved into the file.
4. After scanning and preparing necessary queries and storing it into intermediatory file, now the system connects to destination MySQL database and exports all of the contents reading line by line from the same intermediatory file.
5. During migration, if there exists any issue in data, then system prompts user to correct it and it will again executes the same in destination db.

As a naïve programmer we kept everything simple and uncomplicated. There were no any design patterns involved and there were no any measure of code complexity being done due to why we faced challenges like running out of memory error, when we provide large dataset for our system to process. Also, the file handling, connection pooling and thread allocation was horrible.

As this project is close to my heart, I have thought of revamping this to meet current code standards and practices. Firstly, I will be involved to refactor most of the code and introduce some of the design patterns like Factory, Singleton to make code more reusable and testable. I will be implementing thread in effective way by making use of Java executor framework and will be introducing some unit tests and hopefully if possible integration test in the project.
After that being done, in second phase, I will be doing some architectural changes to distribute the work load from single machine. Currently, there are two major identified processes and both are encapsulated in single system:
	1. Extractor :- This extracts the db metadata and puts it into the file
	2. Migrator :- This migrates the existing data to destination db reading from file

In the following phases, I'll strive to use the fewest files possible. I also have plans to isolate both workers to distinct machines, connecting them via a message broker in the process.  By doing so, the work load will be distributed and I am hoping to get maximum efficiency from respective workers.

I've always believed that learning should never end, and I feel that the process of redesigning this project will teach me a lot about software engineering. I am eager to go down this road.


<img width="709" alt="image" src="https://user-images.githubusercontent.com/29834308/224475056-082bfa83-f928-46d8-b8b9-6275331d9fdc.png">
