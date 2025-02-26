import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import TaskForm from "../components/TaskForm";
import { createTask } from "../api/taskApi"; 

// Mock the createTask API call
jest.mock("../api/taskApi");

describe("TaskForm Component", () => {
  beforeAll(() => {
    Object.defineProperty(window, "location", {
      value: { reload: jest.fn() },
      writable: true,
    });
  });

  afterEach(() => {
    // Clean up mocks after each test
    jest.clearAllMocks();
  });

  it("submits the form and resets inputs", async () => {
    render(<TaskForm />);

    const titleInput = screen.getByPlaceholderText("Title");
    const descInput = screen.getByPlaceholderText("Description");
    const submitButton = screen.getByText("Add");

    fireEvent.change(titleInput, { target: { value: "New Task" } });
    fireEvent.change(descInput, { target: { value: "Task Description" } });

    fireEvent.click(submitButton);

    await waitFor(() => expect(createTask).toHaveBeenCalledWith({
      title: "New Task",
      description: "Task Description",
    }));

    expect(window.location.reload).not.toHaveBeenCalled();

    expect(titleInput).toHaveValue("");
    expect(descInput).toHaveValue("");
  });
});
