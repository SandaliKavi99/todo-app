/* eslint-disable testing-library/no-wait-for-multiple-assertions */
import React from "react";
import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import TaskList from "../components/TaskList";
import { getTasks, markTaskasCompleted } from "../api/taskApi";

jest.mock("../api/taskApi", () => ({
  getTasks: jest.fn(),
  markTaskasCompleted: jest.fn(),
}));

describe("TaskList Component", () => {
  const mockTasks = [
    { id: 1, title: "Task 1", description: "Description 1" },
    { id: 2, title: "Task 2", description: "Description 2" },
  ];

  test("fetches and displays tasks", async () => {
    (getTasks as jest.Mock).mockResolvedValue(mockTasks);
    
    render(<TaskList />);

    await waitFor(() => {
      expect(screen.getByText("Task 1")).toBeInTheDocument();
      expect(screen.getByText("Task 2")).toBeInTheDocument();
    });
  });

  test("marks a task as completed", async () => {
    (getTasks as jest.Mock).mockResolvedValue(mockTasks);
    (markTaskasCompleted as jest.Mock).mockResolvedValue({});

    render(<TaskList />);

    await screen.findByText("Task 1");

    const doneButton = screen.getAllByText("Done")[0];
    fireEvent.click(doneButton);

    expect(markTaskasCompleted).toHaveBeenCalledWith(1);
  });
});
