docker-compose up --build -d

echo "Waiting for database to be ready..."
while ! docker exec -it $(docker ps -qf "name=db") mysqladmin ping --silent; do
  sleep 2
done

docker-compose logs -f
