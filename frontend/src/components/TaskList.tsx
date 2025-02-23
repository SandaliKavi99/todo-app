import React, { useEffect, useState } from "react";
import { getTasks, markTaskasCompleted } from "../api/taskApi.ts";
import "./TaskList.css";

export interface TaskProps {
  id: number;
  title: string;
  description: string;
}

function TaskList() {
  const [tasks, setTasks] = useState<TaskProps[]>([]);

  useEffect(() => {
    async function loadTaskList() {
      const data = await getTasks();
      setTasks(data);
    }
    loadTaskList();
  }, [tasks]);

  const handleDone = async (id: number) => {
    await markTaskasCompleted(id);
    setTasks(tasks.filter((task) => task.id !== id));
  };

  return (
    <ul>
      {tasks.map((task) => (
        <li key={task.id}>
          <div className="task-container">
            <p className="task-name">{task.title}</p>
            <div className="task-content">
              <p>{task.description}</p>
              <div className="done-btn">
                <button onClick={() => handleDone(task.id)}>Done</button>
              </div>
            </div>
          </div>
        </li>
      ))}
    </ul>
  );
}

export default TaskList;
