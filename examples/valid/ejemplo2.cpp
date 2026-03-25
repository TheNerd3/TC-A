int factorial(int n) {
  int res = 1;
  for (int i = 2; i <= n; i++) {
    res = res * i;
  }
  return res;
}

int main() {
  int v = 5;
  int f = factorial(v);
  return 0;
}
