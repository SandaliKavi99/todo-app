# Use Node.js 20.10.0 as the base image
FROM node:20.10.0 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json to install dependencies
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the app's source code
COPY . .

# Build the React app for production
RUN npm run build

# Use Nginx to serve the React app
FROM nginx:alpine

# Copy the React build to the Nginx folder
COPY --from=build /app/build /usr/share/nginx/html

# Expose the port that Nginx will run on
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
