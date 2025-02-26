import React, { useState } from "react";
import { createTask } from "../api/taskApi.ts";
import "./TaskForm.css";

function TaskForm() {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  //form submission
  const handleFormSubmission = async (e: React.FormEvent) => {
    e.preventDefault();
    await createTask({ title, description });
    setTitle("");
    setDescription("");
    window.location.reload();
  };

  return (
    <form className="task-form" onSubmit={handleFormSubmission}>
      <input
        type="text"
        placeholder="Title"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        required
      />
      <input
        className="description-input"
        type="text"
        placeholder="Description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        required
      />
      <div className="submit-btn">
        <button>Add</button>
      </div>
    </form>
  );
}

export default TaskForm;
