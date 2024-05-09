# Proud-s-Collection
share your reviews, and discover your next favorite book. Dive in and start your literary adventure today!

## Table of Contents
- [About the Project](#about-the-project)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Usage](#usage)
  - [Contributing](#contributing)
  - [License](#license)


## About The Project
This project is a collection of books that I have read and reviewed. I have included a brief summary of each book, as well as my personal thoughts and opinions. I hope that this project will inspire others to read more and discover new books that they may not have heard of before.

User has 2 role: User and Admin

`Admin` can do the following:
- Add a new book to the collection
- Every thing that user can do
Note. Right now user need to config the admin through the database manually,
**By changing value on is_accept_consent, and is_enabled column to 1 in users table.**

`User` can do the following:
- View all books in the collection
- View a specific book in the collection
- Add a review for a specific book

## Getting Started
To get a local copy up and running follow these simple steps.

### Prerequisites
This is an example of how to list things you need to use the software and how to install them.

- java 17 or higher
- maven 4 or higher


First, clone the repository to your local machine:
```sh
git clone https://github.com/ProudHokori/Proud-s-Collection.git
```

### Installation

go in the directory of the project:
```sh
cd Proud-s-Collection
```

then open `src/main/java/proud.collection/resourcesn/` directory then change file `.env.example` to `.env` and fill the required fields.

it includes the following fields:
```
SPRING_DATASOURCE_URL=YOUR DATASOURCE URL HERE
SPRING_DATASOURCE_USERNAME=root
GOOGLE_CLIENT_ID=YOUR GOOGLE CLIENT ID HERE
GOOGLE_CLIENT_SECRET=YOUR GOOGLE CLIENT SECRET HERE
SMTP_USERNAME=SMTP USERNAME HERE
SMTP_PASSWORD=SMTP PASSWORD HERE
```

SPRING DATASOURCE URL: the url of the database you are using.
if you are using mysql, the url should look like this:
you can generate it from xampp server or any other server you are using.
```
jdbc:mysql://localhost:3306/your_database_name
```

```
GOOGLE CLIENT ID: the client id of your google account.
GOOGLE CLIENT SECRET: the client secret of your google account.
SMTP USERNAME: the username of the email you are using to send emails.
SMTP PASSWORD: the password of the email you are using to send emails.
```

then run the following command to install the dependencies:
```sh
    mvn install
```

then run the following command to run the project:
```sh
     mvn spring-boot:run
```


### Usage
You can use this project to keep track of the books you have read and reviewed. You can also use it to discover new books and authors that you may not have heard of before. Feel free to share your own reviews and recommendations with others!

### Contributing
Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
6. Wait for the maintainer to review your pull request
7. If it is accepted, it will be merged into the main branch
8. Enjoy your contribution!


### License
Distributed under the MIT License. See `LICENSE` for more information.

---
