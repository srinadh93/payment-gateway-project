FROM node:18-alpine
WORKDIR /app
COPY package.json .
RUN npm install
COPY . .
EXPOSE 80
# Note the port mapping in docker-compose handles 3001:80
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0", "--port", "80"]