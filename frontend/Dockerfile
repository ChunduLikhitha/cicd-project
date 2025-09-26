# Step 1: Build the Vite app
FROM node:18-alpine AS build

WORKDIR /app

# Copy dependency files and install
COPY package*.json ./
RUN npm install

# Copy the rest of the code and build
COPY . .
RUN npm run build

# Step 2: Use Nginx to serve the built files
FROM nginx:alpine

# Copy build output to Nginx default folder
COPY --from=build /app/build /usr/share/nginx/html

# Expose container port
EXPOSE 80

# Run Nginx
CMD ["nginx", "-g", "daemon off;"]
