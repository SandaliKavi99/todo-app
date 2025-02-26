import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import TaskForm from "../components/TaskForm";
import { createTask } from "../api/taskApi";

jest.mock("../api/taskApi", () => ({
  createTask: jest.fn(),
}));

describe("TaskForm Component", () => {
  test("renders form fields correctly", () => {
    render(<TaskForm />);
    
    expect(screen.getByPlaceholderText("Title")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Description")).toBeInTheDocument();
    expect(screen.getByText("Add")).toBeInTheDocument();
  });

  test("submits form with user input", async () => {
    render(<TaskForm />);

    const titleInput = screen.getByPlaceholderText("Title");
    const descriptionInput = screen.getByPlaceholderText("Description");
    const submitButton = screen.getByText("Add");

    fireEvent.change(titleInput, { target: { value: "Test Task" } });
    fireEvent.change(descriptionInput, { target: { value: "Test Description" } });

    fireEvent.click(submitButton);

    expect(createTask).toHaveBeenCalledWith({
      title: "Test Task",
      description: "Test Description",
    });
  });
});
