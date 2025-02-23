import { BASE_URL } from "../constants.ts";

export async function getTasks() {
  const response = await fetch(`${BASE_URL}/list`, {
    method: "GET",
  });
  return response.json();
}

export async function createTask(task: { title: string; description: string }) {
  await fetch(`${BASE_URL}/create`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(task),
  });
}

export async function markTaskasCompleted(id: number) {
  await fetch(`${BASE_URL}/close/${id}`, {
    method: "PUT",
  });
}
