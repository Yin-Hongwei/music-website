import { useConfigureStore } from "@/store/configure";

export function useNavigation() {
  const configureStore = useConfigureStore();

  function changeActiveNav(navName: string) {
    configureStore.setActiveNavName(navName);
  }

  return {
    changeActiveNav,
  };
}
