spotify_clone/
├── src/
│   ├── main/
│   │   ├── java/com/spotify_clone/spotify_clone/
│   │   │   ├── config/       # კონფიგურაციის ფაილები (JwtUtil, SecurityConfig)
│   │   │   ├── controller/   # REST API კონტროლერები
│   │   │   ├── dto/         # Data Transfer Objects
│   │   │   ├── entities/    # JPA ენთითები
│   │   │   ├── enums/       # Enum-ები (UserRole, UserStatus)
│   │   │   ├── exception/   # მორგებული ექსცეფშენები
│   │   │   ├── repositories/# JPA რეპოზიტორიები
│   │   │   ├── service/     # ბიზნეს ლოგიკა
│   │   │   └── util/        # დამხმარე კლასები (RandomCodeGenerator)
│   └── test/
│       └── java/com/spotify_clone/spotify_clone/
│           ├── controller/   # Controller-ების ტესტები
│           ├── service/     # Service-ების ტესტები
├── pom.xml                  # Maven-ის დამოკიდებულებები
└── README.md                # ეს ფაილი

# Spotify Clone

## ძირითადი ფუნქციები
- **მომხმარებლის რეგისტრაცია და ავტორიზაცია**: JWT-ზე დაფუძნებული ავთენტიფიკაცია.
- **პლეილისტის მართვა**: შექმნა, განახლება, წაშლა.
- **მუსიკის ძებნა**: სახელითა და ავტორით ძებნა.
- **ალბომების შექმნა**: არტისტებისთვის ალბომების მართვა.
- **ადმინისტრაცია**: მომხმარებლების ნახვა, განახლება, წაშლა, დაბლოკვა.
- **სტატისტიკა**: მუსიკის მოსმენის სტატისტიკის გენერაცია.

# შემქმნელი: ბექა იმნაძე
