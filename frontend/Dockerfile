# Use Node.js to build the site
FROM node:18-alpine

WORKDIR /app

# Install dependencies
COPY package.json .
RUN npm install

# Copy source code
COPY . .

# Expose port 80 (internal)
EXPOSE 80

# Start the app
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0", "--port", "80"]