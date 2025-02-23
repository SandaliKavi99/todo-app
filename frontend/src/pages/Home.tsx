import React from "react";
import TaskForm from "../components/TaskForm.tsx";
import TaskList from "../components/TaskList.tsx";
import "./Home.css";

function Home() {
  return (
    <div className="home-container">
      <div className="column-container">
        <div className="column" id="col1">
          <div className="taskform-container">
            <p>Add a Task</p>
            <TaskForm />
          </div>
        </div>
        <hr className="divider" />
        <div className="column" id="col2">
          <TaskList />
        </div>
      </div>
    </div>
  );
}

export default Home;
